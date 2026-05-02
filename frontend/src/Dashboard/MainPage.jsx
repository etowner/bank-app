import React, { useContext, useEffect, useState } from "react";
import { Card, Col, Container, Row, Alert, Offcanvas, ListGroup, Modal, Button } from "react-bootstrap";
import { useParams, Link, useNavigate } from "react-router-dom";
import { api } from "../api/axiosConfig";
import Transfer from "../components/Transactions/Transfer";
import AccList from "../components/Home/AccList";
import Header from "../components/Home/Header";
import "../styles/Home.css";
import OpenAccount from "../components/Home/OpenAccount";
import PieChart from "../components/Home/PieChart";
import { UserContext } from "../UserContext";
import "../styles/MainPage.css";
import ProfileManager from "../components/ProfileManager/ProfileManager";
import MainHeader from "./MainHeader";
import DashList from "./DashList";
import Summary from "./Metrics";
import DashOpen from "./DashOpen";
import DashTransfer from "./DashTransfer";

export default function MainPage() {
  const { user, userID, getUser, logout } = useContext(UserContext);
  const [accList, setAccList] = useState([]);
  const [error, setError] = useState(null);

  const openAcc = async (type) => {
    if (accList.length >= 3) {
      setError("You can only have 3 accounts");
      return;
    }
    try {
      const response = await api.post(`/api/v1/account/${type}`);
      // console.log("Opened account:", response.data);
    } catch (error) {
      console.error(error);
    }

   getUser();
  };


  useEffect(() => {
    getUser();
    console.log("userID:", user.userID);
    console.log("user:", user);
    
    setAccList(user.accountList);
    }, []);
    
    // useEffect(() => {
    //   if (user) {
    //     setAccList(user.accountList ?? []);
    //   }
    // }, [user.accountList]);
  

  return (
    <div className="MainPage"> 
        <MainHeader />
        <Container>
            <Row className="mb-1 ps-2">
                <h4 style={{ fontFamily: "Georgia"}}> Hello, {userID}! </h4>
                {/* <h4 style={{ fontFamily: "Georgia", textAlign: "left" }}> {new Date().toLocaleDateString()} </h4 > */}
            </Row>
            <Row className="mb-4">
            {/* 4 cards: Total balance, Income, Spending, Savings */}
            <Col>
                <Card className="metric-card" style={{ borderTop: "2px solid #c9a84c" }}> 
                    <Card.Body>
                        <Card.Title style={{color:"slategrey"}}>Total Balance</Card.Title>
                        <Card.Text style={{ fontFamily:"Georgia", fontSize: "24px", fontWeight: "bold" }}>
                            ${accList.reduce((total, acc) => total + acc.balance, 0)}
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card className="metric-card"> 
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
                <Card className="metric-card"> 
                    <Card.Body>
                        <Card.Title style={{color:"slategrey"}}>Savings</Card.Title>
                        <Card.Text style={{ fontFamily:"Georgia", fontSize: "24px", fontWeight: "bold" }}>
                            ${accList.reduce((total, acc) => total + acc.balance, 0)}
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            </Row>
            <Row xs={1} md={2} lg={2} className="g-4">
                <Col key={1}>  {/* Accounts */}
                    <Card>
                        <DashList accList={accList} />
                        <DashOpen userID={userID} openAcc={openAcc} setError={setError} />
                        {error && (<Alert className="" variant="danger">{error}</Alert>)}
                    </Card>
                </Col>
                {/* Recent transactions (Placeholder: Transfers*/}
                <Col key={2}> <DashTransfer /> </Col>

                {/* Spending by category (Placeholder: Pie Chart) */}
                <Col key={3}>
                    <Card> <PieChart accounts={accList} />  </Card>
                </Col>
            </Row>
        </Container>
            
    </div>
  );
}
