import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)

try:
    import unittest
    from blog_posts.index import app
    
except Exception as e:
    print("Modules are Missing {} ".format(e))


class test_ping(unittest.TestCase):

    # Check for response 200
    def test_ping(self):
        tester = app.test_client(self)
        response = tester.get("/api/ping")
        code = response.status_code
        self.assertEqual(code, 200)

if __name__ == "__main__":
    unittest.main()