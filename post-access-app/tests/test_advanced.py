import os, sys, json
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)

try:
    import unittest
    from blog_posts.index import app
    
except Exception as e:
    print("Modules are Missing {} ".format(e))



class test_advanced(unittest.TestCase):

    # Check for response 200
    def test_sortBy_effect(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=culture&sortBy=id")
        posts = json.loads(response.data)
        self.assertTrue(is_correctly_sorted(posts, "id", "asc"))
        response = tester.get("/api/posts?tags=culture&sortBy=likes")
        posts = json.loads(response.data)
        self.assertTrue(is_correctly_sorted(posts, "likes", "asc"))

    def test_direction_effect(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=culture&direction=asc")
        posts = json.loads(response.data)
        self.assertTrue(is_correctly_sorted(posts, "id", "asc"))
        response = tester.get("/api/posts?tags=culture&direction=desc")
        posts = json.loads(response.data)
        self.assertTrue(is_correctly_sorted(posts, "id", "desc"))

    def test_tags_no_duplication(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=culture&direction=asc")
        posts = json.loads(response.data)
        self.assertTrue(is_not_duplicated(posts, "id"))


def is_not_duplicated(posts, sortBy):
    i=0
    length = len(posts)
    while i < len(posts):
        j=i+1
        while j<length and posts[i][sortBy] == posts[j][sortBy]:
            if is_post_equal(posts[i], posts[j]):
                return False
            j+=1
        i+=1
    return True


def is_post_equal(post1, post2):
    attrs= ["id", "author", "authorId","likes", "popularity", "reads", "tags"]
    for attr in attrs:
        if post1[attr] != post2[attr]:
            return False
    return True


def is_correctly_sorted(posts, sortBy, direction):
    if direction == "asc":
        for i in range(0, len(posts)-1):
            if posts[i][sortBy] > posts[i+1][sortBy]:
                return False
    else:
        for i in range(0, len(posts)-1):
            if posts[i][sortBy] < posts[i+1][sortBy]:
                return False
    return True


if __name__ == "__main__":
    unittest.main()