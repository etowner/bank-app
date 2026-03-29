/* eslint-disable react/prop-types */
import React, { useEffect, useState } from "react";
import { Accordion, useAccordionButton } from "react-bootstrap";
import { Button, Card, Col, Container, Nav, Navbar, Row, Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../api/axiosConfig";
import "../Home/Home.css";
import Deposit from "../Transactions/Deposit";
import Withdraw from "../Transactions/Withdraw";
import CloseAccount from "./CloseAccount";
import LineChart from "./LineChart"

const Account = () => {
  const { accountID, userID } = useParams();
  const [account, setAccount] = useState([]);
  const [type, setType] = useState("");
  const [balance, setBalance] = useState();
  const [transHistory, setTransHistory] = useState([]);

  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/Home/${userID}`);
  };

  function CustomToggle({ children, eventKey }) {
    const decoratedOnClick = useAccordionButton(eventKey, () =>
      console.log("it worked")
    );

    return (
      <Button variant="dark" onClick={decoratedOnClick} className="mb-3">
        {" "}
        {children}
      </Button>
    );
  }

  const updateAccount = (updatedAccount) => {
    setAccount(updatedAccount);
  };

  // Fetch account data
  useEffect(() => {
    api
      .get(`/api/v1/user/${userID}/${accountID}`)
      .then((response) => {
        setAccount(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }, [accountID, userID]);

  // Update account state when account is fetched && when transaction is made
  useEffect(() => {
    if (account) {
      Object.entries(account).forEach(([key, value]) => {
        if (key === "userID") {
        } else if (key === "type") {
          setType(value);
        } else if (key === "balance") {
          setBalance(value);
        } else if (key === "transHistory") {
          setTransHistory(value);
        }
      });
    }
  }, [account, transHistory]);

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
            {type} {accountID}
          </h1>
        </Row>
        <Row className="mb-3">
          <h3>Balance: ${balance} </h3>
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
                      <th>type</th>
                      <th>amount</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transHistory &&
                      Object.entries(transHistory).map(([key, value]) => (
                        <tr key={key}>
                          <td>{key}</td>
                          <td>{value.type}</td>
                          <td>{value.amount}</td>
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
              <Card.Header
                style={{ color: "white", backgroundColor: "#282c34" }}
              >
                <h3>Transaction Options</h3>
              </Card.Header>
              <Card.Body>
                <Accordion defaultActiveKey="2">
                  <CustomToggle eventKey="0">Deposit</CustomToggle>
                  <Col>
                    <Accordion.Collapse eventKey="0">
                      <Card.Body>
                        <Deposit
                          userID={userID}
                          accountID={accountID}
                          updateAccount={updateAccount}
                        />
                      </Card.Body>
                    </Accordion.Collapse>
                  </Col>
                  <Col>
                    <CustomToggle eventKey="1">Withdraw</CustomToggle>
                    <Accordion.Collapse eventKey="1">
                      <Card.Body>
                        <Withdraw
                          userID={userID}
                          accountID={accountID}
                          balance={balance}
                          updateAccount={updateAccount}
                        />
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
            <LineChart accountID={accountID} transHistory={transHistory} />
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
