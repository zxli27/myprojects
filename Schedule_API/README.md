# Schedule_API
[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/zxli27/myprojects)


A Flask-based backend RESTful API of a schedule recording app<br><br>

# Requirements Installation

For local debugging, there are several components to install.<br>
For the the python packages install through pip using the command:

```bash
sudo pip3 install -r requirements.txt
```

The text file contains the necessary packages.<br><br>


```bash
python3 app.py
```
Start a local server at port:5000 using the command above. <br><br>



# Consume the API

## **GET /**


Get infomation of all appointments

```
curl http://127.0.0.1:5000/
```

Return:

* information of all appointments in JSON format

``` json
[
    {
    "time": 2020-3-20,
    "duration": 2,
    "location": Whafe Street
    },
    {
    "time": 2020-10-08,
    "duration": 5,
    "location": Victoria
    }
]

```

---
## **GET /appointment/**


Get the appointment info of the time

```
curl http://127.0.0.1:5000/appointment/?time=<time>
```

Parameters:

* time: STRING  


Example:
```
curl "http://127.0.0.1:5000/appointment/?time=2020-3-20"
```

Return:

* "Cannot find an appointment." if the appointment of the time isn't in the database

* information of the appointment

``` json
{
    "time": 2020-3-20,
    "duration": 2,
    "location": Whafe Street
}
```

---
## **POST /appointment/**


Add a new appointment

```
curl -X POST "http://127.0.0.1:5000/appointment/time=<time>&duration=<duration>&location=<location>"
```

Parameters:

* time: STRING
* duration: INTEGER
* location: STRING

Example:
```
curl -X POST "http://127.0.0.1:5000/appointment/?time=2020-3-20&duration=10&location=Victoria"
```

Return:

* "appointment creation done!"


---
## **PUT /appointment/**


update an appointment

```
curl -X PUT "http://127.0.0.1:5000/appointment/old_time=<old_time>&new_time=<time>&new_duration=<duration>&new_location=<location>"
```

Parameters:

* old_time: STRING
* new_time: STRING (optional)
* new_duration: INTEGER (optional)
* new_location: STRING (optional)

Example:
```
curl -X PUT "http://127.0.0.1:5000/appointment/?old_time=2020-3-20&new_time=2021-1-1&new_duration=6"
```

Return:

* "can't find the old appointment" if the appointment of the time isn't in the database
* new information of the appointment

``` json
{
    "time": 2020-3-20,
    "duration": 2,
    "location": Whafe Street
}
```
---

## **DELETE /appointment/**


delete an appointment

```
curl -X DELETE "http://127.0.0.1:5000/appointment/time=<time>"
```

Parameters:

* time: STRING

Example:
```
curl -X DELETE "http://127.0.0.1:5000/appointment/?time=2020-3-20"
```

Return:

* "can't find the old appointment" if the appointment of the time isn't in the database
* "Appointment deletion done."

---

## **DELETE /delete_all/**


delete all appointments

```
curl -X DELETE "http://127.0.0.1:5000/delete_all/"
```

Parameters:

* 

Example:
```
curl -X DELETE "http://127.0.0.1:5000/delete_all/"
```
Return:

* "All appointments deletion done."

---

Please feel free to contact me at *lizhixiang27@gmail.com* if you have any question.
