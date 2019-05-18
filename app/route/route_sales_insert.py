# coding=utf-8
from app.api.base import base_name as names
from app.api.src.sales import *
from app.api.base.base_router import BaseRouter


class SalesInsert(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.ID_USER, names.ID_NOM, names.ID_SALES]

    def post(self):
        self._read_args()
        return add_sales_user(self.data)

    def put(self):
        self._read_args()
        return get_sales_user(self.data)
