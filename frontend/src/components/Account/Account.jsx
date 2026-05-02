/* eslint-disable react/prop-types */
import React, { useContext, useEffect, useState } from "react";
import { Accordion, useAccordionButton } from "react-bootstrap";
import { Button, Card, Col, Container, Nav, Navbar, Row, Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../api/axiosConfig";
import "../../styles/Home.css";
import Deposit from "../Transactions/Deposit";
import Withdraw from "../Transactions/Withdraw";
import CloseAccount from "./CloseAccount";
import LineChart from "./LineChart"
import { UserContext } from "../../UserContext";

function CustomToggle({ children, eventKey }) {
    const showAction = useAccordionButton(eventKey, () => {});
    
    return (
      <Button variant="dark" onClick={showAction} className="mb-3">
        {" "}
        {children}
      </Button>
    );
  }

const Account = () => {
  const { userID } = useContext(UserContext) 
  const { accountID } = useParams();
  const [account, setAccount] = useState(null);

  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/dash`);
  };

  const updateAccount = (updatedAccount) => {
    setAccount(updatedAccount);
  };

  // Fetch account data
  useEffect(() => {
    api
      .get(`/api/v1/account/${accountID}`)
      .then((response) => {
        setAccount(response.data);
      })
      .catch((error) => {
        console.error(error);
        console.error("Account get error:", error.response ? error.response : error.request ? error.request : error.message);
      });
  }, [accountID, userID]);

  return (
    <div className="Home">
      <Navbar bg="dark" variant="dark" className="mb-5">
        <Container>
          <Navbar.Collapse>
            <Nav className="me-auto">
              <Nav.Link onClick={handleClick}>Back</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container>
        <Row className="mb-3"></Row>
        {/* Account Info */}
        <Row className="mb-3">
          <h1>
            {account?.type} {accountID}
          </h1>
        </Row>
        <Row className="mb-3">
          <h3>Balance: ${account?.balance} </h3>
        </Row>

        <Row className="mb-4 justify-content-md-center">
          <Col sm={6}>
            <Card border="dark" style={{ width: "32rem" }}>
              <Card.Header
                style={{ color: "white", backgroundColor: "#282c34" }}
              >
                <h3>Transaction History</h3>
              </Card.Header>
              <Card.Body>
                <Table bordered hover>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Type</th>
                      <th>Amount</th>
                      <th>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {account?.transHistory &&
                      Object.entries(account.transHistory).map(([key, value]) => (
                        <tr key={key}>
                          <td>{key}</td>
                          <td>{value.type}</td>
                          <td>{value.amount}</td>
                          <td>{new Date(value.timestamp).toLocaleString()}</td>
                        </tr>
                      ))}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
          {/* Account Actions */}
          <Col sm={5}>
            <Card border="dark" style={{ width: "30rem" }}>
              <Card.Header style={{ color: "white", backgroundColor: "#282c34" }}>
                <h3>Transaction Options</h3>
              </Card.Header>
              <Card.Body>
                <Accordion defaultActiveKey="2">
                  <CustomToggle eventKey="0">Deposit</CustomToggle>
                  <Col>
                    <Accordion.Collapse eventKey="0">
                      <Card.Body>
                        <Deposit updateAccount={updateAccount}/> 
                      </Card.Body>
                    </Accordion.Collapse>
                  </Col>
                  <Col>
                    <CustomToggle eventKey="1">Withdraw</CustomToggle>
                    <Accordion.Collapse eventKey="1">
                      <Card.Body>
                        <Withdraw balance={account?.balance} updateAccount={updateAccount}/>
                      </Card.Body>
                    </Accordion.Collapse>
                  </Col>
                </Accordion>
              </Card.Body>
            </Card>
          </Col>
        </Row>
        <Row className="mb-3">
          <Card>
            <LineChart accountID={accountID} transHistory={account?.transHistory} />
          </Card>
        </Row>
        <Row className="md-5 justify-content-md-center">
          <Col>
            <CloseAccount />
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Account;
