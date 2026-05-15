import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';
import { ContextProvider } from './context/context';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter> {/* BrowserRouter component allows routing to be used anywhere within the application b/c the BrowserRouter component is wrapped around the App component. */}
    <ContextProvider>
      <App /> {/* The App component is the first component to be rendered b/c it's the root element. The App component's code is hosted in App.js. */}
    </ContextProvider>
  </BrowserRouter>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
