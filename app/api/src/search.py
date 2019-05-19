from app.api.sql.search_provider import Provider
from app.api.src.matrix import get_data
from app.api.base import base_name as names
import requests
from bs4 import BeautifulSoup
import warnings
warnings.filterwarnings("ignore")


def search_nom(args):
    if args.get(names.FIELD) in ('code', 'name'):
        provider = Provider()
        answer = provider.search_nom(args)
        if args.get(names.FIELD) == 'code' and not answer:
            try:
                id_nom = get_data(args.get('query'))
                update_category(id_nom[0])
            except:
                pass
            answer = provider.search_nom(args)
        return answer or {}
    return {}


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


def update_category(args):
    try:
        data = {
            names.ID_NOM: args.get(names.ID_NOM),
            names.CATEGORY: get_category(args.get(names.CODE))
        }
        provider = Provider()
        provider.update_category(data)
    except:
        pass
