<html>
  <head>
    <meta name="layout" content="mainSinglePage" />
    <title>Mira POS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
     <script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
  </head>
  <body>
    <div class="container">
      <div class="card mx-auto mt-5 align-middle" style="width: 25rem;">
        <div class="card-body pb-3">
          <div class="d-grid gap-1">
         <label for="txtUsername">Username:</label>
          <input type="text" class="form-control" id="txtUsername" placeholder = "Username"/>

          <label for="txtPassword" class="mt-2">Password:</label>
            <input type="password" class="form-control" id="txtPassword"  placeholder = "Password"/>
            <button class="form-control btn btn-primary" type="button" id = "btnLogin">Login</button>
            <small>
              <a href="#">Forgot password?</a>
            </small>
          </div>
        </div>
      </div>
    </div>
    <footer>
   
        <asset:javascript src="login.js"/>
    </footer>
  </body>
</html>