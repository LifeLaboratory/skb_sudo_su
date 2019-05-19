from app.api.base.base_sql import Sql


class Provider:
    @staticmethod
    def get_top_statistic(args):
        sql = """
        select category, "процент_всего" as "percent", "всего" as "all", "просрочка" as "expired"
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
        order by "просрочка" desc, "процент_всего" desc, category
        """
        data = Sql.exec(query=sql, args=args)
        return data

    @staticmethod
    def get_all_statistic(args):
        sql = """
        select "процент_всего" as "percent", "всего" - "просрочка" as "all", "просрочка" as "expired"
        from (
        select *
         , ("просрочка"::float / case when "всего" > 0 then "всего" else 1 end::float)::float "процент_всего"
        from (
        select 
          count(1) filter(where expired) as "просрочка"
          , count(1) as "всего"
          , count(1) filter(where close) as "количество закрытых"
        from user_nom
          left join nomenclature using(id_nom)
        where id_user = {id_user}
        ) n
        ) n
        order by "просрочка" desc, "процент_всего" desc
        """
        data = Sql.exec(query=sql, args=args)
        return data
