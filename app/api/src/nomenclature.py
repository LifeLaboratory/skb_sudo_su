from app.api.sql.nomenclature_provider import Provider
from app.api.base import base_name as names
import datetime


def get_list(args):
    provider = Provider()
    answer = provider.get_list(args)
    return answer


def get_user_list(args):
    provider = Provider()
    answer = provider.get_user_list(args)
    return answer


def get_user_list_expired(args):
    provider = Provider()
    answer = provider.get_user_list_expired(args)
    return answer


def add_nom_in_user(args):
    provider = Provider()
    if args.get(names.EXPIRED_START):
        if args.get(names.EXPIRED_START) == 0 or args.get(names.EXPIRED_START) == '0':
            args[names.EXPIRED_START] = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        else:
            print(args.get(names.EXPIRED_START))
            args[names.EXPIRED_START] = datetime.datetime.utcfromtimestamp(
                int(str(args.get(names.EXPIRED_START))[:-3])).strftime('%Y-%m-%d %H:%M:%S')
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
