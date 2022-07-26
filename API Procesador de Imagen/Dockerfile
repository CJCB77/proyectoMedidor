#Python and linux version
FROM python:3.9-bullseye

#Copy requirements.txt to /app
COPY requirements.txt /app/requirements.txt

#Configure server
RUN set -ex \
    && pip install --upgrade pip \
    && pip install --no-cache-dir  -r /app/requirements.txt 

#Working directory
WORKDIR /app

ADD . .

# EXPOSE 8080

#Run the app with uvicorn
# CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8080"]

#Run the app with uvicorn in heroku
CMD uvicorn main:app --host 0.0.0.0 --port $PORT