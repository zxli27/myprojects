# **Post Access API**
this is a backend JSON API that fetchs data from https://api.hatchways.io/assessment/blog/posts and responses processed data to clients.
<br/>

# Commands
---
## Install Commands
```bash
pip install pipenv
cd blog-posts-app
pipenv install
```
<br/>

## Run Commands
```bash
chmod +x run.sh 
./run.sh
```
<br/>

## Test Commands
```bash
cd tests
python3 run_all_tests.py
```
<br/>

# Features
---
1. make concurrent requests to the API to improve the performance
2. implement a server side cache to store the results of previous API calls  
<br/>

# APIs
---
## **Route 1**
\
**Request**
```
Route: /api/ping  
Method: GET
```
**Response**
```  
    Response body(JSON):  
        {  
            "success": true  
        }  
    Response status code: 200  
```
<br/>

## **Route 2**
\
**Request**
```
Route: /api/posts
Method: GET
```
Query Parameters
| Field     |       Type       |  Description | Default | Example |
|-----------|:----------------:|:-------------:|:-------------:| :-------------:|
| tags      | String, required | A comma seperated list of tags | N/A | health, culture
| sortBy    | String, optional | the field to sort the posts by, one of id, reads, likes, and popularity | id | popularity |
| direction | String, optional | The sort direction, one of asc and desc | asc | asc|

**Correct Response**
```  
    Response body(JSON):  
        [{  
            "id": 1,
            "author": Mike,
            "authorId": 339,
            "likes": 960,
            "popularity": 0.56,
            "reads": 3546,
            "tags": ["tech", "culture"] 
        },
        ...
        ]  
    Response status code: 200  
```
**Error Response**
1. if "tags" parameter is not present:
```
    Response body(JSON):  
        {  
            "error": "Tags parameter is required" 
        }  
    Response status code: 400  
```
2. if a "sortBy" or "direction" are invalid values, specify an error like below:
```
    Response body(JSON):  
        {  
            "error": "sortBy parameter is invalid" 
        }  
    Response status code: 400  
```
<br/>

##Tests
---
## Test Ping
1. **test_ping()**: test if /api/ping works
## Test Basic
1. **test_tags_necessity()**: test if tags parameter is mandatory
2. **test_sortBy_optional()**: test if sortBy parameter is optional
3. **test_direction_optional()**: test if direction parameter is optional
4. **test_sortBy_valid()**: test if sortBy parameter can exceed the required values
5. **test_direction_valid()**: test if direction parameter can exceed the required values
6. **test_tags_format()**: test if tags format, which is tags are seperated by commas, work
## Test Advanced
1. **test_sortBy_effect()**: test if the returned posts are sorted by the sortBy parameter
2. **test_direction_effect()**: test if the returned posts are in the order of the direction parameter
3. **test_tags_no_duplication()**: test if the returned posts have duplictions

---

Please feel free to contact me at *lizhixiang27@gmail.com* if you have any question.