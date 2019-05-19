
import requests
from bs4 import BeautifulSoup
import warnings
import re
from app.api.base.base_sql import Sql
warnings.filterwarnings("ignore")

def get_category(code):
    r = requests.get("http://www.goodsmatrix.ru/goods/" + str(code) +".html")
    print("http://www.goodsmatrix.ru/goods/" + str(code) +".html")
    soup = BeautifulSoup(r.text)
    if "не зарегистрирован в системе." in str(r.text):
        return None
    a = soup.find(id = "ctl00_GroupPath_GroupName")
    a = a.text.split(" / ")
    answer = None
    if len(a) < 3:
        answer = a[-1]
    else:
        answer = a[2]

    return answer


sql = """ SELECT id_nom
  , code
from nomenclature

"""
a = Sql.exec(sql)
for i in a:
    sql = """ Update nomenclature 
              set category = '{category}'
              where id_nom = {id_nom}
    """.format(id_nom = i.get("id_nom"),category = get_category(i.get("code")))
    # print(sql)
    Sql.exec(sql)
