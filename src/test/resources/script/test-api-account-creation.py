import requests
import threading
import time

URL_PREFIX = "http://localhost:4567/api/v1"
REQUESTS_COUNT = 100
THREADS_COUNT = 100


class MyThread(threading.Thread):
    def __init__(self, thread_id, count):
        threading.Thread.__init__(self)
        self.thread_id = thread_id
        self.count = count

    def run(self):
        for _ in range(self.count):
            requests.post(url=URL_PREFIX + "/accounts", headers={'Content-Type': 'application/json'},
                          json={"balance": 1000})


threads = []
for i in range(THREADS_COUNT):
    threads.append(MyThread(i, REQUESTS_COUNT))

start_time = time.time()
for i in range(THREADS_COUNT):
    threads[i].start()

for i in range(THREADS_COUNT):
    threads[i].join(50_000)

elapsed_time = time.time() - start_time
print("Requests ratio: " + str(round(REQUESTS_COUNT * THREADS_COUNT / elapsed_time, 0)) + " req/s")
