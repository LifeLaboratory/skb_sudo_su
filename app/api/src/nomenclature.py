from app.api.sql.nomenclature_provider import Provider


def get_list():
    provider = Provider()
    answer = provider.get_list()
    return answer
