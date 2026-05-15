import { createContext, useState } from 'react';

const Context = createContext(); /* Context has access to the Provider and Consumer components. Context and useContext is for passing values to a child component w/o having to prop drill through each nested child 
component before reaching the desired component. */

//children is the App component
// ContextProvider is a explicit return, arrow function component made for useState logic, and rendering the wrapping of the App component nested within the context.provider component in index.js.
const ContextProvider = ({ children }) => {
    const [isLoggedInContext, setIsLoggedInContext] = useState(false);
    return (
        <Context.Provider value={{isLoggedInContext, setIsLoggedInContext}}>{children}<>{console.log("Context.Provider is rendered.")}</></Context.Provider> // The value prop becomes accessible to every component nested w/ in Context.Provider, using useContext().
    );
};

export { ContextProvider, Context }