from app.api.base.base_sql import Sql


def get_analit(args):
    sql = """select category, "процент_всего"
    from (
    select *
     , ("просрочка"::float / case when "всего" > 0 then "всего" else 1 end::float)::float "процент_всего"
    from (
    select 
        category
      , count(1) filter(where expired) as "просрочка"
      , count(1) as "всего"
      , count(1) filter(where close) as "количество закрытых"
    from user_nom
      left join nomenclature using(id_nom)
    where id_user = {id_user}
    group by category
    ) n
    ) n
    order by "процент_всего" desc, category
    """
    data = Sql.exec(query=sql, args=args)
    return data



