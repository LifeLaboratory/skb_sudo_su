import requests
from bs4 import BeautifulSoup
import warnings
import re
from app.api.base.base_sql import Sql
warnings.filterwarnings("ignore")









def get_data(code):
    r = requests.get("http://www.goodsmatrix.ru/goods/" + str(code) +".html")
    soup = BeautifulSoup(r.text)
    if "не зарегистрирован в системе." in str(r.text) or not bool(soup.find(id = "ctl00_ContentPH_GoodsName")) :
        return None

    try:
        date = soup.find(id = "ctl00_ContentPH_KeepingTime").text.lower()
        if "мес." in date:
            date =re.sub('\мес.$', '', date)  + "months"
        elif "г." in date:
            date = re.sub('\г.$','',date) + "years"
        elif "сут." in date:
            date = re.sub('\сут.$','',date) + "days"
        elif "г" in date:
            date = re.sub('\г$','',date) + "years"
        elif "сут" in date:
            date = re.sub('\сут$','',date) + "days"
        elif "дн." in date:
            date = re.sub('\дн.$','',date) + "days"
        elif "дн" in date:
            date = re.sub('\дн$','',date) + "days"
        elif "мес" in date:
            date = re.sub('\мес$','',date) + "months"
        elif ' ' in date:
            date = '10 year'
    except:
        date = ""

    try:
        gost = soup.find(id="ctl00_ContentPH_Gost").text
    except:
        gost = ""


    try:
        gmo = soup.find(id="ctl00_ContentPH_GmoL").text
    except:
        gmo = ""


    try:
        energy = soup.find(id="ctl00_ContentPH_ESL").text
    except:
        energy = ""

    try:
        packing = soup.find(id="ctl00_ContentPH_PackingType").text
    except:
        packing = ""

    try:
        description =soup.find(id = "ctl00_ContentPH_Comment").text
    except:
        description= ""

    try:
        weight =soup.find(id = "ctl00_ContentPH_Net").text
    except:
        weight = ""



    try:
        storage = soup.find(id = "ctl00_ContentPH_StoreCond").text
    except:
        storage = ""



    data =  {
    "code":code,
    "img":"http://www.goodsmatrix.ru/BigImages/"+code+".jpg",

    "name" : soup.find(id = "ctl00_ContentPH_GoodsName").text,

    "description" :description ,

    "gost":  gost,

    "weight" : weight,

    "shelf_life" : date ,

    "storage_conditions" : storage,

    "gmo" : gmo,

    "packing": packing,

    "energy":energy

    }

    sql_first = """INSERT INTO nomenclature("name","img","shelf_life","code")
                   select 
                     '{name}',
                     '{img}',
                     coalesce('{shelf_life}', '10 year')::interval ,
                     '{code}'
                   RETURNING id_nom
                   
                   
                   """.format(**data)


    id = Sql.exec(sql_first)
    if id:
        data.update({"id":id[0].get("id_nom")})
        sql_second = """INSERT INTO description_nom("id_nom","description","gost","weight","storage_conditions","gmo","packing","energy")
                   VALUES('{id}','{description}','{gost}','{weight}','{storage_conditions}','{gmo}','{packing}','{energy}')""".format(**data)
        Sql.exec(sql_second)