from app.api.sql.info_provider import Provider


def get_info(args):
    provider = Provider()
    answer = provider.get_info(args)
    return answer
