# coding=utf-8
from app.api.base import base_name as names
from app.api.src.nomenclature import *
from app.api.base.base_router import BaseRouter


class NomenclatureUser(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = []

    def get(self, page, id_user):
        args = {
            names.PAGE: page,
            names.ID_USER: id_user
        }
        answer = get_user_list(args)
        return answer
