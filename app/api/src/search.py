from app.api.sql.search_provider import Provider
from app.api.base import base_name as names


def search_nom(args):
    if args.get(names.FIELD) in ('code', 'name'):
        provider = Provider()
        answer = provider.search_nom(args)
        return answer
    return {}
