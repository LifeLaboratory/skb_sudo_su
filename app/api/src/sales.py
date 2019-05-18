from app.api.sql.sales_provider import Provider


def get_sales_user(args):
    provider = Provider()
    answer = provider.get_sales_user(args)
    return answer
