import React from 'react';

function Account(props) {

    return (
        <div>
            <article>
                <h2>{props.accountNo}</h2>
                <h3>{props.accountType}</h3>
                <h4>{props.balance}</h4>
            </article>
        </div>
    );
}

// Components are exported so that they may be used in other components. Components are JSX classes and HTML.
export default Account;