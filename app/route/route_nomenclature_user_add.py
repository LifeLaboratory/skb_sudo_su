# coding=utf-8
from app.api.base import base_name as names
from app.api.src.nomenclature import *
from app.api.base.base_router import BaseRouter


class NomenclatureUserAdd(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.ID_USER, names.ID_NOM, names.ID_USER_NOM,
                     names.EXPIRED_START, names.EXPIRED_END]

    def post(self):
        self._read_args()
        return add_nom_in_user(self.data)

    def put(self):
        self._read_args()
        return update_nom_in_user(self.data)

    def delete(self):
        self._read_args()
        return delete_nom_in_user(self.data)


