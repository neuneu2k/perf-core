PERF-CORE
====================

Introduction
---------------------

Perf-Core is the internal Assoba.fr performance monitoring library.

It is designed to measure "web-time" response times, between 0.1ms and 1m, with a logNormal performance distribution (Aka: a Poisson load distribution).

Detailed statistics can be made from the MeasureVector generated every 5 seconds, a lossy compression designed for logNormal distributions keep the serialized MeasureVector tiny for long term storage.


 
