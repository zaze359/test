//
// Created by 赵臻 on 2020/5/11.
//
import fcntl
from contextlib import contextmanager
import logging

logger = logging.getLogger(__name__)

@contextmanager
def flocked(lock_file):
    with open(lock_file, 'w') as fd:
        try:
            # Lock file. Raise OsError if failed to lock.
            fcntl.flock(fd, fcntl.LOCK_EX | fcntl.LOCK_NB)
            logger.info('Acquired lock for %s', lock_file)
            yield
        finally:
            fcntl.flock(fd, fcntl.LOCK_UN)
            logger.info('Released lock for %s', lock_file)

try:
    with flocked('/tmp/lock_file'):
        # Do something.
except OsError:
       logger.exception('Failed to lock file')
