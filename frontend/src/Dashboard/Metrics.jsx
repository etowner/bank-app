
import React, { useContext } from "react";
import { Card, Col } from "react-bootstrap";
import { UserContext } from "../UserContext";

export default function Metrics({ accList }) {
    const { user } = useContext(UserContext);

    return (
        <div>
            <Col>
                <Card> 
                    <Card.Body>
                        <Card.Title style={{color:"slategrey"}}>Total Balance</Card.Title>
                        <Card.Text style={{ fontFamily:"Georgia", fontSize: "24px", fontWeight: "bold" }}>
                            ${accList.reduce((total, acc) => total + acc.balance, 0)}
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card className="metric-card"   > 
                    <Card.Body>
                        <Card.Title style={{color:"slategrey"}}>Income</Card.Title>
                        <Card.Text style={{ fontFamily:"Georgia", fontSize: "24px", fontWeight: "bold" }}>
                            ${accList.reduce((total, acc) => total + acc.balance, 0)}
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card className="metric-card"> 
                    <Card.Body>
                        <Card.Title style={{color:"slategrey"}}>Spending</Card.Title>
                        <Card.Text style={{ fontFamily:"Georgia", fontSize: "24px", fontWeight: "bold" }}>
                            ${accList.reduce((total, acc) => total + acc.balance, 0)}
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card> 
                    <Card.Body>
                        <Card.Title style={{color:"slategrey"}}>Savings</Card.Title>
                        <Card.Text style={{ fontFamily:"Georgia", fontSize: "24px", fontWeight: "bold" }}>
                            ${accList.reduce((total, acc) => total + acc.balance, 0)}
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
        </div>
    );
}