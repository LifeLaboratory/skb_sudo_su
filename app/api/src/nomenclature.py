from app.api.sql.nomenclature_provider import Provider
from app.api.base import base_name as names


def get_list(args):
    provider = Provider()
    answer = provider.get_list(args)
    return answer


def get_user_list(args):
    provider = Provider()
    answer = provider.get_user_list(args)
    return answer


def add_nom_in_user(args):
    provider = Provider()
    answer = provider.add_nom_in_user(args)
    return answer


def update_nom_in_user(args):
    provider = Provider()
    if args.get(names.EXPIRED_END) and args.get(names.EXPIRED_START):
        provider.update_expired(args)
    elif args.get(names.EXPIRED_START):
        provider.update_expired_start(args)
    elif args.get(names.EXPIRED_END):
        provider.update_expired_end(args)
    return {}


def delete_nom_in_user(args):
    provider = Provider()
    provider.delete_expired(args)
    return {}
