from app.api.src.matrix import *


with open("product","r") as f:
    lines = f.readlines()


for i in lines:
    j = i.split("/")
    code = j[len(j) - 1].split(".")[0]
    get_data(code)