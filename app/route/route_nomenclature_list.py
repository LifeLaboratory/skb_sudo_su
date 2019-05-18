# coding=utf-8
from app.api.base import base_name as names
from app.api.src.nomenclature import *
from app.api.base.base_router import BaseRouter


class NomenclatureList(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = []

    def get(self, page):
        args = {
            names.PAGE: page
        }
        answer = get_list(args)
        return answer
