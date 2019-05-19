from app.api.sql.search_provider import Provider
from app.api.src.matrix import get_data
from app.api.base import base_name as names


def search_nom(args):
    if args.get(names.FIELD) in ('code', 'name'):
        provider = Provider()
        answer = provider.search_nom(args)
        if args.get(names.FIELD) == 'code' and not answer:
            get_data(args.get('query'))
            answer = provider.search_nom(args)
        return answer or {}
    return {}
