import threading
import time
import requests

URL_PREFIX = "http://localhost:4567/api/v1"
REQUESTS_COUNT = 2000
THREADS_COUNT = 1


class MyThread(threading.Thread):
    def __init__(self, thread_id, count, source_id, dest_id):
        threading.Thread.__init__(self)
        self.thread_id = thread_id
        self.count = count
        self.source_id = source_id
        self.dest_id = dest_id

    def run(self):
        for _ in range(self.count):
            requests.post(url=URL_PREFIX + "/accounts/" + str(self.source_id) + "/transactions",
                          headers={'Content-Type': 'application/json'},
                          json={'relatedAccountId': self.dest_id, 'amount': -1.0})


r = requests.post(url=URL_PREFIX + "/accounts", headers={'Content-Type': 'application/json'},
                  json={"balance": 1_000_000})
account_a_id = r.json().get('id')

r = requests.post(url=URL_PREFIX + "/accounts", headers={'Content-Type': 'application/json'},
                  json={"balance": 1_000_000})
account_b_id = r.json().get('id')

threads = []
for i in range(THREADS_COUNT):
    threads.append(MyThread(i, REQUESTS_COUNT, account_a_id, account_b_id))

start_time = time.time()
for i in range(THREADS_COUNT):
    threads[i].start()

for i in range(THREADS_COUNT):
    threads[i].join(50_000)

elapsed_time = time.time() - start_time
print("Requests ratio: " + str(round(REQUESTS_COUNT * THREADS_COUNT / elapsed_time, 0)) + " req/s")
