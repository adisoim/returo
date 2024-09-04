import React from "react";
import { useState } from "react";
import Button from "./Button";
import axios from "axios";

export default function Management() {
  const [dateInput, setDateInput] = useState("");

  function handlePdfCreationAndPrint() {
    let data = JSON.stringify({
      date: dateInput,
    });

    let config = {
      method: "post",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/api/generateAndPrintDailyTotalPdf",
      headers: {
        "Content-Type": "application/json",
      },
      data: data,
    };

    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function handlePdfCreationAndPrintVoucher() {
    let data = JSON.stringify({
      date: dateInput,
    });

    let config = {
      method: "post",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/api/generateAndPrintDailyVoucherTotalPdf",
      headers: {
        "Content-Type": "application/json",
      },
      data: data,
    };

    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function printPartialTotalCash() {
    let config = {
      method: "get",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/api/partialTotalReceipt",
      headers: {},
    };

    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function printPartialTotalVoucher() {
    let config = {
      method: "get",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/api/partialTotalVoucher",
      headers: {},
    };

    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
      })
      .catch((error) => {
        console.log(error);
      });
  }

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 lg:px-8">
      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6">
          <Button onClick={handlePdfCreationAndPrint}>
            Creeaza si printeaza totalul pentru plata numerar
          </Button>
          <Button onClick={handlePdfCreationAndPrintVoucher}>
            Creeaza si printeaza totalul pentru vouchere
          </Button>
          <Button onClick={printPartialTotalCash}>
            Total partial plata numerar
          </Button>
          <Button onClick={printPartialTotalVoucher}>
            Total partial vouchere
          </Button>

          <input
            type="date"
            onChange={(e) => setDateInput(e.target.value)}
            value={dateInput}
            className="input"
          ></input>
        </form>
      </div>
    </div>
  );
}
