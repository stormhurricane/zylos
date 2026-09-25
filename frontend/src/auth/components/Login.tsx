import { Link } from 'react-router-dom';

export const Login =()  => {
    return ( 
      <div >
        <div>
          <label htmlFor="username">Username</label>
          <input id="username" name="username" type="text"></input>
        </div>
        <div >
          <label>Password</label>
          <input type="password"></input>
        </div>
        <button>Login</button>

        <p>
          Don't have an account? <Link to="/register">Register</Link>
        </p>
      </div>
    );
};
