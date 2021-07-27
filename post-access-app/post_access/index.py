import requests
import json
from flask import Flask, jsonify, request
from concurrent.futures import ThreadPoolExecutor


app = Flask(__name__)

#a dictionary whose key is a tag and value is the posts corresponding to the tag.
cache = {}


@app.route("/api/ping")
def ping():
    return jsonify({"success": True}), 200

@app.route("/api/posts")
def get_posts():
    try:
        tags, sortBy, direction, err = get_paras()
    except Exception as e:
        return jsonify({"error": str(e)}), 400

    
    unsorted_posts = fetch_posts_concurrently(tags)

    sorted_posts = sorted(unsorted_posts, key = lambda x: x[sortBy], reverse= True if direction=="desc" else False)
    sorted_posts = remove_duplicated(sorted_posts)

    return jsonify(sorted_posts), 200

# get values from request query string and report error if exists
def get_paras():

    tags = request.args.get('tags')
    if tags is None:
        raise Exception("Tags parameter is required")
    tags = tags.split(',')


    sortBy = request.args.get('sortBy')
    sortBy = "id" if sortBy is None else sortBy

    if sortBy not in ["id", "likes", "popularity", "reads"]:
        raise Exception("sortBy parameter is invalid")


    direction = request.args.get('direction')
    direction = "asc" if direction is None else direction

    if direction != "asc" and direction != "desc":
        raise Exception("direction parameter is invalid")
    
    return tags, sortBy, direction, 0

#fetch posts from data source concurrently and achieve cache
def fetch_posts_concurrently(tags):
    unsorted_posts = []
    cached_tags = [tag for tag in tags if tag in cache]
    urls = ["https://api.hatchways.io/assessment/blog/posts?tag="+tag for tag in tags if tag not in cache]
    pool = ThreadPoolExecutor(max_workers=5)
    for response in pool.map(lambda url: requests.get(url), urls):
        loaded_response = json.loads(response.text)["posts"]
        cache[response.url.split('=')[1]] = loaded_response
        unsorted_posts.extend(loaded_response)

    
    for tag in cached_tags:
        unsorted_posts.extend(cache[tag])

    return unsorted_posts

#remove duplicated elements from the sorted posts
def remove_duplicated(posts):
    new_posts = []
    length = len(posts)
    i=0
    while i < length:
        new_posts.append(posts[i])
        j=i+1
        while j<length and is_post_equal(posts[i], posts[j]):
            j+=1
        i=j
    return new_posts

#compare two posts
def is_post_equal(post1, post2):
    attrs= ["id", "author", "authorId","likes", "popularity", "reads", "tags"]
    for attr in attrs:
        if post1[attr] != post2[attr]:
            return False
    return True

if __name__ == "__main__":
    app.run()