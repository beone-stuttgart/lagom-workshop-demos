# Usage of wishlist crawler to check from commandline:

## Crawl wishlist
```
curl -i -X POST -H "Content-Type:application/json" -d '{"presentId": "1", "wishlistUrl": "https://www.amazon.de/gp/registry/wishlist/VO2Y19FT1WHJ/ref=cm_wl_list_o_1?"}' "http://127.0.0.1:26607/wishlist/import"

{ "done" : true }
```

## Get wishlist
```
curl -i -X GET "http://127.0.0.1:26607/wishlist/query/1"

{"presentId":"1","success":true,"items":[{"imageUrl":"https://images-eu.ssl-images-amazon.com/images/I/416C5JrV0OL._SL500_PIsitb-sticker-arrow-big,TopRight,35,-73_OU03_SL135_.jpg","name":"Korpuslinguisti​k (LIBAC, Band 3433)","price":"EUR 6,99","link":"/dp/3825234339?_encoding=UTF8&colid=VO2Y19FT1WHJ&coliid=IAMLM76DNTDFB"}]}
```