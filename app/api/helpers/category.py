
import requests
from bs4 import BeautifulSoup
import warnings
import re
from app.api.base.base_sql import Sql
warnings.filterwarnings("ignore")

def get_category(code):
    r = requests.get("http://www.goodsmatrix.ru/goods/" + str(code) +".html")
    soup = BeautifulSoup(r.text)
    if "не зарегистрирован в системе." in str(r.text):
        return None
    a = soup.find(id = "ctl00_GroupPath_GroupName")

    return a.text.split(" / ")[2]
print( get_category("5449000228963"))


sql = """ SELECT id_nom
from nomenclature

"""
a = Sql.exec(sql)
for i in a:
    sql = """ Update nomenclature 
              set category = '{category}'
              where id_nom = {id_nom}
    """.format(id_nom = i.get("id_nom"),category = get_category(i.get("id_nom")))
    Sql.exec(sql)