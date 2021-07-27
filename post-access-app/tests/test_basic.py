import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)

try:
    import unittest
    from blog_posts.index import app
    
except Exception as e:
    print("Modules are Missing {} ".format(e))



class test_basic(unittest.TestCase):

    # Check for response 200
    def test_tags_necessity(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts")
        code = response.status_code
        self.assertEqual(code, 400)
        response = tester.get("/api/posts?tags=foo")
        code = response.status_code
        self.assertEqual(code, 200)

    def test_sortBy_optional(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=foo")
        code = response.status_code
        self.assertEqual(code, 200)
        response = tester.get("/api/posts?tags=foo&sortBy=id")
        code = response.status_code
        self.assertEqual(code, 200)
    
    def test_direction_optional(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=foo")
        code = response.status_code
        self.assertEqual(code, 200)
        response = tester.get("/api/posts?tags=foo&direction=asc")
        code = response.status_code
        self.assertEqual(code, 200)
    
    def test_sortBy_valid(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=foo&sortBy=id")
        code = response.status_code
        self.assertEqual(code, 200)
        response = tester.get("/api/posts?tags=foo&sortBy=author")
        code = response.status_code
        self.assertEqual(code, 400)

    def test_direction_valid(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=foo&sdirection=asc")
        code = response.status_code
        self.assertEqual(code, 200)
        response = tester.get("/api/posts?tags=foo&direction=rise")
        code = response.status_code
        self.assertEqual(code, 400)

    def test_tags_format(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=foo,void,bar")
        code = response.status_code
        self.assertEqual(code, 200)

    def test_more_paras(self):
        tester = app.test_client(self)
        response = tester.get("/api/posts?tags=foo,void,bar&sortBy=likes&direction=asc&author=Mike")
        code = response.status_code
        self.assertEqual(code, 200)


if __name__ == "__main__":
    unittest.main()