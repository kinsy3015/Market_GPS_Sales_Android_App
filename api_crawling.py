# 최종 코드
import requests
import re
import pprint
from bs4 import BeautifulSoup
import pandas as pd
import sys
import json
import xmltodict
import time
from firebase import firebase
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from pprint import pprint
import datetime, calendar

# 파이어베이스 연동
firebase = firebase.FirebaseApplication("https://pricetag-c7b1c.firebaseio.com/", None)
# 날짜 계산
from dateutil.parser import parse as dateparse

lastFriday = datetime.date.today()
oneday = datetime.timedelta(days=1)
while lastFriday.weekday() != calendar.FRIDAY:
    lastFriday -= oneday

lastFriday = lastFriday - datetime.timedelta(days=7)
lastFriday = str(lastFriday)
goodInspectDay = lastFriday.translate(str.maketrans('', '', '-'))
# API KEY 및 URL 할당
open_api_key =
'liqoCXpTHOFv9LIJ1vw%2FF17je5JsTUIXvmPyXJqIzX0EhqWhon3okMjRdpyYg1N%2BKi
ch4X % 2
BANvhdmA8DU3YduA % 3
D % 3
D
'
params = ''
urlSearchAll = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/
getProductInfoSvc.do?ServiceKey = ' + open_api_key
"(전체상품목록조회)"
urlSearchProductCode = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/
getStandardInfoSvc.do?classCode = AL & ServiceKey = ' + open_api_key
"(상품 소분류 코드 조회)"
urlSearchLocalCode = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/
getStandardInfoSvc.do?classCode = AR & ServiceKey = ' + open_api_key
"(지역 소분류 코드 조회하기)"
urlProduct = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/
getProductInfoSvc.do?ServiceKey = ' + open_api_key
"(상품 상세정보 조회하기)"
urlLocal = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/
getStoreInfoSvc.do?ServiceKey = ' + open_api_key
"(판매지역 상세정보 조회하기)"
# 전체 상품 목록 조회
response = requests.get(urlSearchAll)
status = response.status_code
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response = dict_type["response"]
dictSearchAll = dict_response["result"]
# 상품 소분류 코드 조회
response = requests.get(urlSearchProductCode)
status = response.status_code
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response = dict_type["response"]
dict_result = dict_response["result"]
dictSearchProductCode = dict_result['iros.openapi.service.vo.stdInfoVO']
# 지역 소분류 코드 조회
response = requests.get(urlSearchLocalCode)
status = response.status_code
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response = dict_type["response"]
dict_result = dict_response["result"]
dictSearchLocalCode = dict_result['iros.openapi.service.vo.stdInfoVO']
# 전체 상품목록, 소분류 코드 데이터 삽입
result6 = firebase.post('/pricetag-c7b1c/goodInfo', dictSearchAll)
result6 = firebase.post('/pricetag-c7b1c/goodCategoryInfo', dictSearchProductCode)
result6 = firebase.post('/pricetag-c7b1c/areaCategoryInfo', dictSearchLocalCode
# 상세정보 조회 및 데이터 삽입 파싱
res = requests.get(urlProduct)
res2 = requests.get(urlLocal)
soup = BeautifulSoup(res.content, 'html.parser')
soup2 = BeautifulSoup(res2.content, 'html.parser')
tag = 'iros.openapi.service.vo.entpinfovo'
data = soup.find_all('item')
data2 = soup2.find_all(tag)
# 날짜별 업데이트해서 상품 상세정보 삽입
i = 0
best = 0
for item in data:
    g_id = item.find("goodid").get_text()
urlProductDetail = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/
getProductPriceInfoSvc.do?goodInspectDay = ' + goodInspectDay + ' & goodId = ' + g_id +
                                                                             '&ServiceKey=' + open_api_key
response = requests.get(urlProductDetail)
status = response.status_code
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
if best < int(g_id):
    best = int(g_id)
time.sleep(1.0)
i = i + 1
try:
    dict_response = dict_type["response"]
    dict_result = dict_response["result"]
    dictGoodDetail = dict_result['iros.openapi.service.vo.goodPriceVO']
    resultGoodEntpInfo = firebase.post('/pricetag-c7b1c/goodEntpInfo' +
                                       str(goodInspectDay) + '/' + str(g_id), dictGoodDetail)
    except:
    i = i - 1
    continue
firebase.post('/pricetag-c7b1c/goodEntpInfotestNumbering' + str(best) + '/' + str(best),
              str(best))
# 각 판매점 별로 좌표가 있는 데이터 삽입
i = 0
for tag in data2:
    entp_id = tag.find("entpid").get_text()
    urlLocalDetail = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/
getStoreInfoSvc.do?entpId = ' + entp_id + ' & ServiceKey = ' + open_api_key
response2 = requests.get(urlLocalDetail)
status2 = response2.status_code
text2 = response2.text
dict_type2 = xmltodict.parse(text2)
json_type2 = json.dumps(dict_type2)
dict_type2 = json.loads(json_type2)
time.sleep(1.0)

i = i + 1
try:
    dict_response2 = dict_type2["response"]
dict_result2 = dict_response2["result"]
dictEntpResult = dict_result2['iros.openapi.service.vo.entpInfoVO']
resultEntpInfo = firebase.post('/pricetag-c7b1c/entpDetail/' + str(entp_id),
                               dictEntpResult)
except:
i = i - 1
continue
firebase.post('/pricetag-c7b1c/entpGoodInfoNumbering/' + str(i), str(i))
# 데이터 삭제(실제로 활용하지 않은 코드)
firebase.remove('/pricetag-c7b1c/goodEntpInfotestNumbering20201127/')