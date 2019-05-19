# coding=utf-8
from app.api.base import base_name as names
from app.api.src.info import *
from app.api.base.base_router import BaseRouter


class Info(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.QUERY]

    def get(self, id_nom):
        args = {
            names.ID_NAME: id_nom
        }
        answer = get_info(args)
        return answer or {}
