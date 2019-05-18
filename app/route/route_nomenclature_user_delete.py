# coding=utf-8
from app.api.base import base_name as names
from app.api.src.nomenclature import *
from app.api.base.base_router import BaseRouter


class NomenclatureUserDelete(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.ID_USER, names.ID_NOM, names.ID_USER_NOM,
                     names.EXPIRED_START, names.EXPIRED_END]

    def delete(self, id_user, id_user_nom):
        args = {
            names.ID_USER: id_user,
            names.ID_USER_NOM: id_user_nom
        }
        delete_nom_in_user(args)
        return {}


