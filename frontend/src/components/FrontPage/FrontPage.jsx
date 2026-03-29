import React from "react";
import { Container, Row } from "react-bootstrap";
import AccountBox from "./AccountBox";
import "./FrontPage.css";

const FrontPage = () => {

  return (
  
      <div className="FrontPage">
        <div className="FrontPage-header">
          <h1 className="md-3">Bank Application</h1>
        </div>
        <Container
          fluid
          className="d-flex flex-column justify-content-center"
          style={{ color: "white", minHeight: "95vh" }}
        >
          <Row className="justify-content-center">
            <AccountBox />
          </Row>
        </Container>
      </div>
  
  );
};

export default FrontPage;
