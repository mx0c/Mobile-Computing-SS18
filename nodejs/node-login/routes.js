'use strict';

const auth = require('basic-auth');
const jwt = require('jsonwebtoken');

const register = require('./functions/register');
const login = require('./functions/login');
const profile = require('./functions/profile');
const password = require('./functions/password');
const config = require('./config/config.json');
const user = require('./models/user');

module.exports = router => {

	router.get('/', (req, res) => res.end('Welcome to PosiTime!'));


	router.post('/authenticate', (req, res) => {

		const credentials = auth(req);
        const loggedUser = null;


		if (!credentials) {

			res.status(400).json({ message: 'Invalid Request !' });

		} else {
			login.loginUser(credentials.name, credentials.pass)

			.then(result => {
                const loginResult = result;
                login.getUser(credentials.name)
                            .then(result => {
                                console.log("Result: " + result);
                                const token = jwt.sign(loginResult, config.secret, { expiresIn: "365d" });


                                //Response
                                console.log("Status Code: " + loginResult.status);
                                console.log("User: " + result);
                                console.log("Email for User: " + loginResult.message);
                                console.log("Token: " + token);


                                res.status(loginResult.status).json({ user: JSON.stringify(result), message: loginResult.message, token: token });
                            })
			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	router.post('/register', (req, res) => {
		const email = req.body.email;
		const password = req.body.password;
		const firstname = req.body.firstName;
		const lastname = req.body.lastName;


		if (!lastname || !firstname || !email || !password || !email.trim() || !password.trim()) {

			res.status(400).json({message: 'Invalid Request !'});

		} else {

			register.registerUser(email, password, firstname, lastname)

			.then(result => {

				res.setHeader('Location', '/users/'+email);
				res.status(result.status).json({ message: result.message })
			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});


	router.post('/token/:id', (req, res) =>{
	    console.log( req.headers['x-access-token']);
	    if(checkToken(req)) {
	        //Valid Token
	        res.status(200).json({ message: "Token is valid"});
	    } else {
	        //Token not valid
	        res.status(401).json({ message: 'Invalid Token !' });
	    }
	});

	function checkToken(req) {

    		const token = req.headers['x-access-token'];

    		if (token) {

    			try {

      				var decoded = jwt.verify(token, config.secret);
      				console.log(decoded);
      				console.log(req.params.id);

      				return decoded.message === req.params.id;

    			} catch(err) {

    				return false;
    			}

    		} else {

    			return false;
    		}
    	}



    /* NOT USED YET
	router.get('/users/:id', (req,res) => {

		if (checkToken(req)) {

			profile.getProfile(req.params.id)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.put('/users/:id', (req,res) => {

		if (checkToken(req)) {

			const oldPassword = req.body.password;
			const newPassword = req.body.newPassword;

			if (!oldPassword || !newPassword || !oldPassword.trim() || !newPassword.trim()) {

				res.status(400).json({ message: 'Invalid Request !' });

			} else {

				password.changePassword(req.params.id, oldPassword, newPassword)

				.then(result => res.status(result.status).json({ message: result.message }))

				.catch(err => res.status(err.status).json({ message: err.message }));

			}
		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.post('/users/:id/password', (req,res) => {

		const email = req.params.id;
		const token = req.body.token;
		const newPassword = req.body.password;

		if (!token || !newPassword || !token.trim() || !newPassword.trim()) {

			password.resetPasswordInit(email)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			password.resetPasswordFinish(email, token, newPassword)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});



	*/
}