# coding=utf-8
from app.api.base import base_name as names
from app.api.src.sales import *
from app.api.base.base_router import BaseRouter


class SalesDelete(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.ID_USER, names.ID_NOM, names.ID_SALES]

    def delete(self, id_user, id_sales):
        args = {
            names.ID_USER: id_user,
            names.ID_SALES: id_sales
        }
        delete_sales_user(args)
        return {}
