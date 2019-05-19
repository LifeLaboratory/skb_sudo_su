# coding=utf-8
from app.api.base import base_name as names
from app.api.src.search import *
from app.api.base.base_router import BaseRouter


class Search(BaseRouter):

    def __init__(self):
        super().__init__()
        self.args = [names.QUERY]

    def get(self, field, query):
        args = {
            names.FIELD: field,
            names.QUERY: query
        }
        answer = search_nom(args)
        return answer or {}
