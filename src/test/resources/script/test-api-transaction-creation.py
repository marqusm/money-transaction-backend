import threading
import time

import requests

URL_PREFIX = "http://localhost:4567/api/v1"
REQUESTS_COUNT = 1000
THREADS_COUNT = 5


class MyThread(threading.Thread):
    def __init__(self, thread_id, count, source_acc, dest_acc):
        threading.Thread.__init__(self)
        self.thread_id = thread_id
        self.count = count
        self.source_acc = source_acc
        self.dest_acc = dest_acc

    def run(self):
        for _ in range(self.count):
            requests.post(url=URL_PREFIX + "/accounts/" + self.source_acc["id"] + "/transactions",
                          headers={'Content-Type': 'application/json'},
                          json={'relatedAccountId': self.dest_acc["id"], 'amount': -1.0})


r = requests.post(url=URL_PREFIX + "/accounts", headers={'Content-Type': 'application/json'},
                  json={"balance": 1_000_000})
account_a = r.json()

r = requests.post(url=URL_PREFIX + "/accounts", headers={'Content-Type': 'application/json'},
                  json={"balance": 1_000_000})
account_b = r.json()
initial_amount = account_a["balance"] + account_b["balance"]

threads = []
for i in range(THREADS_COUNT):
    threads.append(MyThread(i, REQUESTS_COUNT, account_a, account_b))

start_time = time.time()
for i in range(THREADS_COUNT):
    threads[i].start()

for i in range(THREADS_COUNT):
    threads[i].join(50_000)

elapsed_time = time.time() - start_time
print("Requests ratio: " + str(round(REQUESTS_COUNT * THREADS_COUNT / elapsed_time, 0)) + " req/s")

r = requests.get(url=URL_PREFIX + "/accounts/" + account_a["id"])
account_a = r.json()

r = requests.get(url=URL_PREFIX + "/accounts/" + account_b["id"])
account_b = r.json()
final_amount = account_a["balance"] + account_b["balance"]

print("Final balance difference: " + str(final_amount - initial_amount))

r = requests.get(url=URL_PREFIX + "/accounts/" + account_a["id"] + "/transactions")
print("Final transactions count difference: " + str(r.json()["count"] - REQUESTS_COUNT * THREADS_COUNT) + " (" + str(
    r.json()["count"] / (REQUESTS_COUNT * THREADS_COUNT) * 100) + " %)")
