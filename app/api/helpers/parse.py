import requests
from bs4 import BeautifulSoup
import warnings
import re

warnings.filterwarnings("ignore")





def get_data(code):
    r = requests.get("http://www.goodsmatrix.ru/goods/" + str(code) +".html")
    soup = BeautifulSoup(r.text)
    if "не зарегистрирован в системе." in str(r.text):
        return None

    try:
        date = soup.find(id = "ctl00_ContentPH_KeepingTime").text
        if "мес." in date:
            date =re.sub('\мес.$', '', date)  + "months"
        elif "г." in date:
            date = re.sub('\г.$','',date) + "years"
        elif "сут.":
            date = re.sub('\сут.$','',date) + "days"
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


    if not bool(soup.find(id = "ctl00_ContentPH_GoodsName")):
        return None
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
    return "http://www.goodsmatrix.ru/goods/" + str(code) +".html"



import csv


def csv_dict_reader(file_obj):
    """
    Read a CSV file using csv.DictReader
    """
    #import re
    #file = open(file_obj)

    # reader = csv.DictReader(file_obj)
    # for line in reader:
    #     print(line["Name"])
import re
k = 0
for i in range(101,545):
    #0000 1 11 111
    print(i)
    if i > 99:
        with open("DATA/uhtt_barcode_ref_0" + str(i) + ".csv") as f_obj:
            # dict = csv_dict_reader(f_obj)
            data = f_obj.readlines()
    elif i>9:
        with open("DATA/uhtt_barcode_ref_00" + str(i) + ".csv") as f_obj:
            # dict = csv_dict_reader(f_obj)
            data = f_obj.readlines()
    else:
        with open("DATA/uhtt_barcode_ref_000" + str(i) + ".csv") as f_obj:
            # dict = csv_dict_reader(f_obj)
            data = f_obj.readlines()

    for i in data:
        if "Продукт" in i:

            d = get_data(i.split()[1])
            if d:
                print("*" * 30, d)
                with open ("product.log","a") as f:
                    f.write(d  +  "\n")
































# sql_first = """INSERT INTO description_nom("name","img","shelf_life","code")
#                VALUES('{name}','{img}','{shelf_life}','{code}')""".format(**data[0])
#
# sql_second = """INSERT INTO nomenclature("name","img","shelf_life","code","description","gost","weight","storage_conditions","gmo","packing","energy")
#                VALUES('{name}','{img}','{shelf_life}','{code}','{description}','{gost}','{weight}','{storage_conditions},'{gmo}'','{packing}','{energy})'""".format(**data[0])



#name,photo,shelf_life,codet