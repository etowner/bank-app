import React from "react";
import { Button, Card, Col, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export default function DashList({ accList}) {
    const navigate = useNavigate();
    function handleAccount(accountID) {
        navigate(`/account/${accountID}`);
    }
    return (
        <div>
          
            <Card.Header style={{ fontSize: "13px", fontWeight: "500", color: "#282c34",  backgroundColor: "transparent"}}>
                <h2>Accounts</h2>
            </Card.Header>
            <Card.Body>
            {accList &&
                accList.map((account) => {
                return (
                    <h3 key={account.accountID}>
                        <Row className="mb-3">
                            <Col > {account.type} - {account.accountID}</Col>
                            <Col style={{ fontFamily:"Georgia" }} className="justify-content-end">
                                ${account.balance}
                                <Button variant="link"  onClick={() => handleAccount(account.accountID)}>
                                    View
                                </Button>
                            </Col>
                        </Row>
                    </h3>
                );
                })}
            </Card.Body>
        
        </div>
    );
}