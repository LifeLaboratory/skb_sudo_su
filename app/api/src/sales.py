from app.api.sql.sales_provider import Provider


def get_sales_user(args):
    provider = Provider()
    answer = provider.get_sales_user(args)
    return answer or {}


def add_sales_user(args):
    provider = Provider()
    answer = provider.add_sales_user(args)
    return answer or {}


def delete_sales_user(args):
    provider = Provider()
    answer = provider.delete_sales_user(args)
    return answer or {}
