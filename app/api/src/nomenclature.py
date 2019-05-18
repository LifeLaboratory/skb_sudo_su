from app.api.sql.nomenclature_provider import Provider


def get_list(args):
    provider = Provider()
    answer = provider.get_list(args)
    return answer


def get_user_list(args):
    provider = Provider()
    answer = provider.get_user_list(args)
    return answer
