from app.api.sql.profile_provider import Provider


def get_profile(args):
    provider = Provider()
    answer = provider.get_profile(args)[0]
    return answer
