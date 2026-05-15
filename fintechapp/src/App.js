import logo from './logo.svg';
import './App.css';
import React, { useEffect, useState, useContext } from 'react';
import { Navigate, Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import { Context } from './context/context';
import Home from './components/Home';
import Login, { Logout } from './components/Login'; // No brackets for a default export. Brackets for named exports.
import Account from './components/Account';

// Functional components are named beginning w/ an uppercase letter, and returns a single JSX element.
function App() {

  console.log("App is rendered.");

  const location = useLocation();
  const navigate = useNavigate();
  const [accountDetails, setAccountDetails] = useState([]); // accountDetails is the state variable and is initialized to an empty array; setAccountDetails is its setter function. Invoking setAccountDetails replaces accountDetails' value.
  const paths = ["/", "/emilefinancialservices/login", "/home/accounts"];

  // De-structuring of the context from the Context.Provider component.
  const {
    isLoggedInContext,
    setIsLoggedInContext
  } = useContext(Context);

  useEffect(() => {
    // Nested if statements to ensures that when not logged in, the only webpages that should be accessible are Home and Login.
    if (!isLoggedInContext) {
      // Checks if the current webpage is a known route of this application.
      if (paths.includes(location.pathname)) {
        // Checks if the current webpage is not the Home webpage.
        if (location.pathname !== "/") {
          // Checks if the current webpage is not the Login webpage.
          if (location.pathname !== "/emilefinancialservices/login") {
            navigate("/emilefinancialservices/login"); // Redirects the current webpage to the Login webpage.
          }
        }
      }
    } else if (isLoggedInContext) {
      navigate("/home/accounts"); // Redirects the current webpage to the Accounts webpage after logging in.
    }
  }, [isLoggedInContext, location.pathname, navigate]);

  // handler is invoked for as many times an object is sent from the Login component, and obj holds one object at a time.
  const handler = (obj) => {
    accountDetails.push(obj);
  };

  return (
    <div className="App">
      {isLoggedInContext && <Logout />} {/* Conditional statement meaning, if the left side of && is true, perform the right side. */}
      <Routes>
        <Route path="/" element={<Home exact={true} />} /> {/* Maps the path of the URI to a component. */}
        <Route path="/emilefinancialservices/login" element={<Login accountDetails={accountDetails} setAccountDetails={handler} />} /> {/* Passes setIsLoggedIn as a prop (aka prop drilling) to allow the value of isLoggedIn to be changed from the Login component. */}
        <Route path="/home/accounts" element={accountDetails.map((eachEntry) => <Account balance={eachEntry.bal} accountType={eachEntry.accType} accountNo={eachEntry.accNo} key={eachEntry.id} />)} />
        <Route path="*" element={<Navigate replace to="/" />} /> {/* Conditional statement meaning, if the left side of && is true, perform the right side. The astericks represents every path. Therefore, element's value has to be conditional to account for any unknown webpages; in which case, element will redirect the current webpage to the Home webpage. */}
      </Routes>
    </div>
  );
};

export default App;
