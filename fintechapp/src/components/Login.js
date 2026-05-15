import React, { useEffect } from 'react';
import { useState } from 'react';
import axios from 'axios';
import { Context } from '../context/context';
import { useCookies } from 'react-cookie';

const Login = (props) => {

    console.log("Login is rendered.");

    const {
        isLoggedInContext,
        setIsLoggedInContext
    } = React.useContext(Context);

    const [accountData, setAccountData] = useState([]);
    const [xsrfToken] = useState([]);

    // useCookies can only access cookies whose Httponly flag is not set; cookies whose path and domain attributes do not match the current URL, are inaccessible to the useCookies hook.
    const [cookies] = useCookies(['XSRF-TOKEN']);
    const csrfToken = cookies['XSRF-TOKEN'];

    const parser = new DOMParser();

    // JavaScript object/Json.
    const data = {
        username: "",
        password: ""
    };

    axios.get('https://localhost/emilefinancialservices/login', { withCredentials: true })
    .then((response) => {
        xsrfToken[1] = parser.parseFromString(response.data, 'text/html')?.querySelector('input[name="_csrf"]').value;
        console.log(xsrfToken[1]);
    });

    const params = new URLSearchParams();
    
    const handleSubmit = (e) => {

        // Prevents the form from submitting, so it won't interfere w/ ajax.
        e.preventDefault();
        
        params.append('username', data.username);
        params.append('password', data.password);
        
        // The URL of the RESTful API endpoint is where 'params' is sent to. 
        // The records are retrieved from the backend in Json format. Axios' post method corresponds to the HTTP Request POST Method. 
        // .then means the promise is resolved, and .catch means promise is rejected.
        // withCredentials set to true to allow cookies to be sent and received across domains.
        // https://localhost/emilefinancialservices/login is Spring Security's default login endpoint.
        // axios.post("localhost:8080/oauth2/token")
        //     .then(() => {
                axios.post("https://localhost/emilefinancialservices/login", params, {
                    withCredentials: true,
                    headers: {
                        'X-CSRF-Token': xsrfToken[1],
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                    .then((res) => {
                        console.log(res);
                        for (let m = 0; m < res.data.length; m++) { // res.data.length is the length of the first dimension of the array and res.data[0].length is the length of the second dimension of the array.
                            let count = m + 1;
                            accountData[m] =
                            {
                                bal: res.data[m][0],
                                accType: res.data[m][1],
                                accNo: res.data[m][2],
                                id: count
                            };
                        }
                        accountData.map((eachEntry) => props.setAccountDetails(
                            {
                                bal: eachEntry.bal,
                                accType: eachEntry.accType,
                                accNo: eachEntry.accNo,
                                id: eachEntry.id
                            }
                        ));
                        setIsLoggedInContext(true);
                        // xsrfToken[1] = (xsrfToken[1] === parser.parseFromString(response.data, 'text/html')?.querySelector('input[name="_csrf"]').value) ? xsrfToken[1] : parser.parseFromString(response.data, 'text/html')?.querySelector('input[name="_csrf"]').value;
                        // axios.post("https://localhost/emilefinancialservices/user/id", data, {
                        //     withCredentials: true,
                        //     headers: {
                        //         'X-CSRF-Token': xsrfToken[1],
                        //         // 'Authorization': `Bearer ${bearerToken}`,
                        //         'Content-Type': 'application/json'
                        //     }
                        // })
                        // .then((res) => {
                        //     for (let m = 0; m < res.data.length; m++) { // res.data.length is the length of the first dimension of the array and res.data[0].length is the length of the second dimension of the array.
                        //         let count = m + 1;
                        //         accountData[m] =
                        //         {
                        //             bal: res.data[m][0],
                        //             accType: res.data[m][1],
                        //             accNo: res.data[m][2],
                        //             id: count
                        //         };
                        //     }
                        //     accountData.map((eachEntry) => props.setAccountDetails(
                        //         {
                        //             bal: eachEntry.bal,
                        //             accType: eachEntry.accType,
                        //             accNo: eachEntry.accNo,
                        //             id: eachEntry.id
                        //         }
                        //     ));
                        //     setIsLoggedInContext(true);
                        // })
                        // .catch((error) => {
                        //     if (error.response.status === 401) {
                        //         console.error("Unauthorized");
                        //     }
                        //     if (error.response.status === 403) {
                        //         console.error("Forbidden");
                        //     }
                        //     if (error.response.status === 404) {
                        //         console.error("Not Found");
                        //     }
                        //     if (error.response.status === 500) {
                        //         console.error("Internal Server Error");
                        //     }
                        // });
                })
                .catch((error) => {
                    if (error.response.status === 401) {
                        console.error("Unauthorized");
                    }
                    if (error.response.status === 403) {
                        console.error("Forbidden");
                    }
                    if (error.response.status === 404) {
                        console.error("Not Found");
                    }
                    if (error.response.status === 500) {
                        console.error("Internal Server Error");
                    }                    
                });
            // })
            // .catch((error) => {
            //     if (error.response.status === 401) {
            //         console.error("Unauthorized");
            //     }
            //     if (error.response.status === 403) {
            //         console.error("Forbidden");
            //     }
            //     if (error.response.status === 404) {
            //         console.error("Not Found");
            //     }
            //     if (error.response.status === 500) {
            //         console.error("Internal Sever Error");
            //     }
            // });
        // }
        
    }

    // The render method returns JSX/HTML that as a result, are displayed to the browser.
    return (
        <div>
            <form>
                <input type="text" placeholder=" Enter Username" onChange={(e) => (data.username = e.target.value)} /> {/* Input field's value is captured as it changes, and is then initialized to 'userName'. */}
                <input type="password" placeholder=" Enter Password" onChange={(e) => (data.password = e.target.value)} />
                <button onClick={handleSubmit}>Login</button> {/* The 'handleSubmit' method is invoked when the button is clicked. */}
            </form>
        </div>
    );
};

export const Logout = () => {

    console.log("Logout is rendered.");

    const {
        setIsLoggedInContext
    } = React.useContext(Context);

    return (
        <button onClick={() => setIsLoggedInContext(false)}>Logout</button>
    );
}

// Components are exported so that they may be used in other components.
export default Login;
