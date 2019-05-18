from app.api.sql.static_provider import Provider


def get_top_statistic(args):
    provider = Provider()
    answer = provider.get_top_statistic(args)
    return answer


def get_all_statistic(args):
    provider = Provider()
    answer = provider.get_all_statistic(args)
    return answer
