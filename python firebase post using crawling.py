#!/usr/bin/env python
# coding: utf-8

# In[20]:


import requests
import re
import pprint
from bs4 import BeautifulSoup
import pandas as pd
import sys
import json
import xmltodict

#from firebase import firebase
#import firebase_admin
#from firebase_admin import credentials
#from firebase_admin import db
#from pprint import pprint


# In[21]:


open_api_key = 'liqoCXpTHOFv9LIJ1vw%2FF17je5JsTUIXvmPyXJqIzX0EhqWhon3okMjRdpyYg1N%2BKich4X%2BANvhdmA8DU3YduA%3D%3D'

params =''

url = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/getProductInfoSvc.do?ServiceKey=' + open_api_key

"(전체상품목록조회)"


# In[22]:


url2 = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/getStoreInfoSvc.do?ServiceKey=' + open_api_key

"(판매지역정보불러오기)"


# In[23]:


url3 = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/getProductPriceInfoSvc.do?goodInspectDay=20100205&entpId=10&ServiceKey=' + open_api_key

"(해당 판매업체에서 판매하는 상품 조회)"


# In[24]:


url4 = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/getProductPriceInfoSvc.do?goodInspectDay=20100205&goodId=31&ServiceKey=' + open_api_key

"(해당 상품을 어디서, 얼마에 판매중인지 조회)"


# In[25]:


url5 = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/getStandardInfoSvc.do?classCode=AL&ServiceKey=' + open_api_key

"(상품 소분류 코드 조회)"


# In[26]:


url6 = 'http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService/getStandardInfoSvc.do?classCode=AR&ServiceKey=' + open_api_key

"(지역 소분류 코드 조회하기)"


# In[27]:


""" testing firebase
data = {
    'Name':'kkk',
    'Phone':'01041515'
}

result = firebase.post('/aaaaa-4f8e6/cc',data)
"""


# In[28]:


res = requests.get(url)
soup = BeautifulSoup(res.content, 'html.parser')
data = soup.find_all('item')
rows = []
for item in data:
    g_id = str(item.find("goodid").text)
    g_name = str(item.find("goodname").text)
    g_pdcode = str(item.find("productentpcode").text)
    g_unit = str(item.find("goodunitdivcode").text)
    g_cnt = str(item.find("goodbasecnt").text)
    g_code = str(item.find("goodsmlclscode").text)
    g_detail= str(item.find("detailmean"))
    g_detail= re.sub('<.+?>', '', g_detail, 0).strip()
    g_totalcnt = str(item.find("goodtotalcnt").text)
    g_totaldiv = str(item.find("goodtotaldivcode").text)
    
    rows.append({"상품아이디":g_id,
                "상품명":g_name,
                "제조업체코드":g_pdcode,
                "상품_단위_구분_코드":g_unit,
                 "상품_단위_량":g_cnt,
                 "상품_소분류_코드":g_code,
                 "제품_설명_상세":g_detail,
                 "상품_용량":g_totalcnt,
                 "상품_용량_구분_코드":g_totaldiv})


        
    
columns = ["상품아이디","상품명","제조업체코드","상품_단위_구분_코드","상품_단위_량","상품_소분류_코드","제품_설명_상세","상품_용량","상품_용량_구분_코드"]
catalog_cd_df = pd.DataFrame(rows,columns=columns)
cd= catalog_cd_df.fillna("no data")
with pd.option_context('display.max_rows', None, 'display.max_columns', None):
    print(cd)
    


# In[29]:


response = requests.get(url) 
status = response.status_code 
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response=dict_type["response"]
dict1=dict_response["result"]
dict1_good=dict1["item"]
print(dict1_good)


# In[30]:


response = requests.get(url2) 
status = response.status_code 
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response=dict_type["response"]
dict_result=dict_response["result"]
dict2= dict_result['iros.openapi.service.vo.entpInfoVO']
print(dict2)


# In[31]:


response = requests.get(url3) 
status = response.status_code 
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response=dict_type["response"]
dict_result=dict_response["result"]
dict3 = dict_result['iros.openapi.service.vo.goodPriceVO']
print(dict3)


# In[32]:


response = requests.get(url4) 
status = response.status_code 
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response=dict_type["response"]
dict_result=dict_response["result"]
dict4 = dict_result['iros.openapi.service.vo.goodPriceVO']
print(dict4)


# In[33]:


response = requests.get(url5) 
status = response.status_code 
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response=dict_type["response"]
dict_result=dict_response["result"]
dict5=dict_result['iros.openapi.service.vo.stdInfoVO']
print(dict5)


# In[35]:


response = requests.get(url6) 
status = response.status_code 
text = response.text
dict_type = xmltodict.parse(text)
json_type = json.dumps(dict_type)
dict_type = json.loads(json_type)
dict_response=dict_type["response"]
dict_result=dict_response["result"]
dict6=dict_result['iros.openapi.service.vo.stdInfoVO']
print(dict6)


# In[36]:


print(len(dict1_good))
print(len(dict2))
print(len(dict3))
print(len(dict4))
print(len(dict5))
print(len(dict6))

entlist = []

for x in range(0,len(dict1)) :
    if(dict2[x]['entpAreaCode']=='020100000'):
        print(dict2[x])
        aa=json.dumps(dict2[x])
        entlist.append(aa)
        
        
print(entlist)


# In[ ]:


firebase = firebase.FirebaseApplication("https://wbb-beb41.firebaseio.com/",None)


# In[1]:


"""

파이썬으로 어떻게 불러올것인가?

데이터 정리부분

우리가 데이터를 불러올 부분


======검색하는 경우======

ex)사과

사과가 포함된 모든 물건을 검색해야하므로 => ‘전체상품목록’ 불러와야함

불러와서 저장 후, 사과가 포함된 리스트 검색

이름 순으로 정렬 후 리스트로 노출

해당 상품을 누르면

해당상품의 아이디를 통해 가격정보를 ‘판매업체기준조회’ 로 불러와야함




ex)동작구 (혹은 어떤 지명)(시, 군, 구 단위로만 검색가능)

‘동작구(혹은 어떤 지명)의 좌표’를 카카오 api에서 받아옴

‘판매지역정보’를 불러옴

동작구(혹은 어떤 지명) 반경으로 좌표를 검색해서 해당 업체를 검색해서 리스트로 보여줌

업체를 클릭시 -> 가격정보불러오기(판매업체기준조회)



ex) 필터

필터의 종류1 : 상품 소분류 코드 정보 불러옴 // **검색 이후에 필터 적용 시 일반 필터처럼 해당 상품만 남아야함

ex2) 상품 필터만 적용하는 경우 -> ‘전체상품목록’ 불러와서 해당 코드만 조회하고 해당하는 상품을 리스트에 노출

ex3) 사과 검색 후 필터 ‘차, 음료’ 적용시 사과식초, 햇사과, 사과아이스크림 등은 제외하고 사과주스, 사과차만 남도록해야

ex4) 동작구 검색 필터 ‘치즈’ 적용시 불러온 업체에서 치즈 카테고리를 남겨서 검색


필터의 종류2 : 지명 코드 불러옴 // ** 검색 이후에 필터 적용 시 일반 필터처럼 해당 지역의 업체만 남아야함


"""


# In[18]:




print(len(dict1_good))
for x in range(0,len(dict1_good)-300) :
    
    dict1_good[x]['goodName']
    print(dict1_good[x]['goodName'])
    
    img_url = "https://www.google.com/search"

    params = {
            'q': dict1_good[x]['goodName'],
            'tbm':'isch'
        }


    res = requests.get(img_url,params)
    soup = BeautifulSoup(res.content, 'lxml')
    img_tag=soup.select('img')
    img_src=soup.find('img')



# print(type(img_tag))
    print(img_tag[1]['src'])
    dict1_good[x]['src'] =img_tag[1]['src']
#     print(dict1_good[x])

    
    
    
    


# print(img_tag[1].find(attrs="src"))
# print(img_src)
# print(img_src.attrs)

print(dict1_good)


# In[19]:


print(dict1_good)


# In[ ]:


result1 = firebase.post('/wbb-beb41/good',dict1_good)
result2 = firebase.post('/wbb-beb41/entp',dict2)
result3 = firebase.post('/wbb-beb41/price',dict3)
result4 = firebase.post('/wbb-beb41/price2',dict4)
result5 = firebase.post('/wbb-beb41/stdinfo',dict5)
result6 = firebase.post('/wbb-beb41/area',dict6)

