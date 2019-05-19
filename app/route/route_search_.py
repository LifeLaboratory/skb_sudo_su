# coding=utf-8
from app.api.base import base_name as names
from app.api.src.nomenclature import *
from app.api.base.base_router import BaseRouter


class Search_(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.QUERY]

    def get(self, field):
        args = {
            names.PAGE: 0
        }
        answer = get_list(args)
        return answer or {}
